Next are the step to set up the Minikube environment.
 > minikube start

You will get next messages when you use `docker` commands:
 
 `Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?`

To address that, you will then need to point, the terminal in which you are, to the minikube Docker-daemon instance:
 > minikube docker-env

Read the output of the comand and you will then need to execute:
 > eval $(minikube -p minikube docker-env)

When running Docker on Minikube, all our interactions from the localhost will need to point to the VM created
by Minikube where Docker, and Kubernetes cluster, run.
First, get the IP of that VM:
> minikube ip

Then, add it to to your `hosts` file so you can refer to the IP with the domain you defined. This will be helpful, 
for your local producer and consumers, to be able to interact with the container running in Minikube.

`192.168.59.100  host.minikube.internal`

Once you have Minikube running, and you can access `docker` commands on it, now it is time to start the Kafka container.

- `Dockerfile` includes the image which will install and start kafka
  - Run `docker build  -t kafka .` to build a Docker image and tag as `kafka`.
  - Run `docker run --name kafkacontainer -p 9092:9092 -h kafkahost --rm -it kafka` to run the container for the kafka image.
    - We need to publish port 9092, where kafka server exposes its services.
    - We especify a hostname (`-h`) so the container gets an addressble name.


- `startKafka.sh` is the script to include in the Docker image and to execute as ENTRYPOINT which starts up Zookeeper, Kafka and creates a topic.


