#!/bin/sh
cat ../download/output/*.ttl > store.ttl
cat ../linking/internal/*.nt >> store.ttl
cat ../linking/external/*.nt >> store.ttl
