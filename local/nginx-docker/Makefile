rm:
	docker rmi nginx/thesis
build:
	docker build -t nginx/thesis .
run-in-console:
	docker run -it --rm --name nginx_thesis --publish 80:80 --publish 443:443 --add-host=host.docker.internal:host-gateway nginx/thesis
run-in-console-shell:
	docker run -it --rm --name nginx_thesis --publish 80:80 --publish 443:443 --add-host=host.docker.internal:host-gateway nginx/thesis /bin/sh
run-detached:
	docker run -d --name nginx_thesis --publish 80:80 --publish 443:443 --add-host=host.docker.internal:host-gateway nginx/thesis
stop:
	docker stop nginx_thesis
start:
	docker start nginx_thesis
