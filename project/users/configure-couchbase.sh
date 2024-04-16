#!/bin/bash
set -e

# Inicia o servidor Couchbase
echo "Starting Couchbase Server..."
/opt/couchbase/bin/couchbase-server -- -noinput -kernel global_enable_tracing false &

# Aguarda o servidor Couchbase iniciar
echo "Waiting for Couchbase to start..."
until curl -s http://localhost:8091/ui/index.html; do
  sleep 1
  echo "Waiting for the Couchbase UI to become available..."
done

echo "Couchbase is ready."

# Configura o cluster, apenas se não estiver configurado
if ! curl -s -u $COUCHBASE_SERVER_USERNAME:$COUCHBASE_SERVER_PASSWORD http://localhost:8091/pools/default; then
  echo "Configuring cluster..."
  /opt/couchbase/bin/couchbase-cli cluster-init \
    --cluster-username $COUCHBASE_SERVER_USERNAME \
    --cluster-password $COUCHBASE_SERVER_PASSWORD \
    --cluster-name myCluster \
    --cluster-ramsize 1024 \
    --services data,index,query,fts \
    --index-storage-setting default
else
  echo "Cluster already initialized."
fi

# Cria o bucket apenas se ele não existir
bucket_info=$(/opt/couchbase/bin/couchbase-cli bucket-list -c localhost:8091 -u $COUCHBASE_SERVER_USERNAME -p $COUCHBASE_SERVER_PASSWORD | grep $COUCHBASE_BUCKET || true)
if [ -z "$bucket_info" ]; then
  echo "Creating bucket..."
  /opt/couchbase/bin/couchbase-cli bucket-create \
    --cluster localhost:8091 \
    --username $COUCHBASE_SERVER_USERNAME \
    --password $COUCHBASE_SERVER_PASSWORD \
    --bucket $COUCHBASE_BUCKET \
    --bucket-type couchbase \
    --bucket-ramsize 512 \
    --enable-flush 1 \
    --bucket-replica 0  # Assume 0 replicas if the environment does not support more
  echo "Bucket $COUCHBASE_BUCKET created successfully."
else
  echo "Bucket $COUCHBASE_BUCKET already exists."
fi

# Mantém o servidor em execução
wait
