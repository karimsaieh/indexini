docker-compose -f docker-compose-staging.yml stop
docker-compose -f docker-compose-staging.yml pull
docker-compose -f docker-compose-staging.yml up -d