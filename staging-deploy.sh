docker-compose -f docker-compose-staging.yml stop
docker-compose -f docker-compose-staging.yml build
docker-compose -f docker-compose-staging.yml -d up