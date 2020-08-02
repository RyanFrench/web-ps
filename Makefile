docker_build:
	docker build -t web-ps .

docker_run:
	docker run -ti -p 8080:8080 web-ps

vagrant_up:
	vagrant up --provider=virtualbox
