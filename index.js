const fs = require('fs');
const _ = require('lodash');
const json2csv = require('json2csv');

const readFilePromise = (filename) => {
  return new Promise((resolve, reject) => {
    fs.readFile(filename, 'utf-8', (err, data) => {
      if (err) {
        reject(err);
      } else {
        resolve(data);
      }
    })
  })
}

const hunspellToObject = (hunspell) => {
  return hunspell
    .split('\n')
    .slice(1)
    .filter(line => line.length > 0)
    .reduce((acc, line) => {
      const [word, tags] = line.split('/');
      acc[word] = {
        word,
        tags
      };
      return acc;
    }, {})
}

const frequenciesToObject = (frequencies) => {
  return frequencies
    .split('\n')
    .reduce((acc, line) => {
      const [word, freq] = line.split(' ');
      if (word && word != '') {
        acc[word] = Number(freq);
      }
      return acc;
    }, {})
}

const stockToObject = (stock) => {
  return JSON.parse(stock).reduce((acc, val) => {
    const attributes = val;
    const word = val.title;
    delete attributes.title;
    acc[word] = attributes;
    return acc;
  }, {})
}

const filterOnlyCompleteRecords = (words) => {
  return _.fromPairs(
    _.toPairs(words).filter(([word, data]) => {
      return data.pos && _.values(data).every(v => v !== '');
    })
  );
}

Promise.all([
  readFilePromise('./node_modules/dictionary-cs/index.dic')
    .then(data => hunspellToObject(data)),
  readFilePromise('./node_modules/frequency_words/content/2016/cs/cs_full.txt')
    .then(data => frequenciesToObject(data)),
  readFilePromise('./node_modules/stopwords/dist/cs.json')
    .then(data => JSON.parse(data)),
  readFilePromise('./node_modules/word-stock-dump/cs.json')
    .then(data => stockToObject(data)),
])
  .then(([hunspell, frequencies, stopwords, wiktionaryData]) => {
    return _.fromPairs(
      _.toPairs(hunspell)
        .map(([word, data]) => {
          return [
            word,
            {
              ...data,
              stopword: stopwords.includes(word) ? true : false,
              freq: _.get(frequencies, word, 0),
              ...wiktionaryData[word]
            }
          ]
        })
    )
  })
  .then(words => filterOnlyCompleteRecords(words))
  .then(words => _.fromPairs(
    _.toPairs(words).map(([word, data]) => {
      delete data.tags;
      data.pos = data.pos.split(', ').slice(0, 1)[0];
      return [word, data];
    })
  ))
  .then(words => {
    const csv = json2csv({ data: _.values(words) });
    return new Promise((resolve, reject) => {
      fs.writeFile('semestral/source/out.csv', csv, function (err) {
        if (err) {
          reject(err);
        }
        else {
          console.log('file saved');
          resolve()
        }
      });
    })
  })
  ;
