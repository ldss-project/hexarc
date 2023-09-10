/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.jahrim.hexarc.architecture.vertx.core.dsl

import io.github.jahrim.hexarc.architecture.vertx.core.components.VertxService
import io.github.jahrim.hexarc.architecture.vertx.core.components.VertxService
import io.vertx.core.Verticle

/**
 * A DSL for deploying [[Verticle]] services, following the best practices of
 * the Hexagonal Architecture.
 *
 * See [[VertxService]] for more information.
 *
 * @example Deploying a single service: {{{
 *    DeploymentGroup.deploySingle(Vertx.vertx()) {
 *      new Service:
 *        name = "LampService"
 *
 *        new Port[LampPort]:
 *          model = LampModel()
 *
 *          new Adapter(LampHttpAdapter())
 *          new Adapter(LampMqttAdapter())
 *    }
 * }}}
 */
object VertxDSL:
  export VertxDSLExtensions.*
  export Vocabulary.*

  /** The [[VertxDSL]] vocabulary. */
  private object Vocabulary:
    export io.github.jahrim.hexarc.architecture.vertx.core.dsl.contexts.{
      DeploymentGroupDSLContext as DeploymentGroup,
      ServiceDSLContext as Service,
      PortDSLContext as Port,
      AdapterDSLContext as Adapter
    }
