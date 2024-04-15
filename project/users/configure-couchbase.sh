#!/bin/bash

# Espera o serviço do Couchbase iniciar completamente
until $(curl --output /dev/null --silent --head --fail http://localhost:8091); do
    printf "."
    sleep 5
done

sleep 10

# Verifica se o cluster já está inicializado
if couchbase-cli server-list -c couchbase --username=Administrator --password=password | grep -q 'active'; then
    echo "Cluster is already initialized"
else
    # Inicializa o cluster
    couchbase-cli cluster-init -c couchbase --cluster-username=Administrator --cluster-password=password --services=data,index,query
    echo "Cluster initialized successfully"
fi

# Cria o bucket se ainda não existir
if couchbase-cli bucket-list -c couchbase --username=Administrator --password=password | grep -q 'users-bucket'; then
    echo "Bucket already exists"
else
    couchbase-cli bucket-create -c couchbase --username=Administrator --password=password --bucket=users-bucket --bucket-type=couchbase --bucket-ramsize=100
    echo "Bucket created successfully"
fi
