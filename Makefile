.PHONY: help up down build run test clean logs local

help:
	@echo "Available targets:"
	@echo "  help   - Show this help message"
	@echo "  up     - Start Docker containers (docker-compose up -d)"
	@echo "  down   - Stop and remove Docker containers (docker-compose down)"
	@echo "  test   - Run Maven tests"
	@echo "  clean  - Clean Maven project and remove target directory"


up:
	docker compose up -d

down:
	docker compose down


test:
	mvn test

clean:
	mvn clean
	rm -rf target
