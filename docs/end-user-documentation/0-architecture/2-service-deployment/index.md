---
title: Deploying a service
layout: default
nav_order: 3
parent: Architecture Module
---

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Deploying a single service

In HexArc, services are deployed by `DeploymentGroup`s, which are part of the `VertxDSL`.

A `DeploymentGroup` is the configuration for the deployment of a group of services.

In particular, it allows the deployment of a single service using `DeploymentGroup.deploySingle`,
which takes the definition of a service, deploys an instance of the service and returns the
`Deployment` of that instance, which can be used to execute the un-deployment later.

### Example
{: .no_toc}

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.Deployment
import io.vertx.core.{Vertx, Future}
import scala.util.{Try, Success, Failure}

// Deployment
val futureDeployment: Future[Deployment] =
  DeploymentGroup.deploySingle(Vertx.vertx()) { myColoredLampService }
  
val tryDeployment: Try[Deployment] =
  futureDeployment.await

val deployment: Deployment =
  tryDeployment match
    case Success(d) => { println("Deployment successful"); d }
    case Failure(e) => throw Error("Deployment failed")

// Un-deployment
val futureUndeployment: Future[Void] =
  deployment.undeploy()

val tryUndeployment: Try[Void] =
  futureUndeployment.await

tryUndeployment match
  case Success(_) => println("Un-deployment successful")
  case Failure(e) => throw Error("Un-deployment failed")
```

Full example [here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/architecture/vertx/core/dsl/SingleDeploymentTest.scala).

## Deploying multiple services

A `DeploymentGroup` can also be created explicitly, allowing for any number of services
to be deployed at once.

When deployed, a `DeploymentGroup` returns a `DeploymentMap`, which is a map from service
names to lists of deployments. In fact, many service instances can be bound to the same name
(e.g. service replicas...).
The `DeploymentMap` can be used to undeploy the services later.

### Example
{: .no_toc}

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.Deployment
import io.vertx.core.{Vertx, Future}

// Service Definition
def idProviderService: DeploymentGroup ?=> Service = ???
def namedService: DeploymentGroup ?=> Service = ???

// Deployment
val deploymentGroup: DeploymentGroup = 
  new DeploymentGroup(Vertx.vertx()):  // Deploy on <Vertx.vertx()>
    idProviderService                  // an IdProviderService
    namedService                       // a NamedService
    namedService                       // and another NamedService

val deploymentMap: Future[Map[String, Seq[Deployment]]] =
  deploymentGroup.deploy()
```

Full example [here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/architecture/vertx/core/dsl/GroupDeploymentTest.scala).
