/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.wedatasphere.linkis.gateway.security

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.webank.wedatasphere.linkis.common.utils.{Logging, Utils}
import com.webank.wedatasphere.linkis.gateway.config.GatewayConfiguration._
import com.webank.wedatasphere.linkis.gateway.exception.GatewayErrorException
import org.apache.commons.lang.StringUtils

object ProxyUserUtils extends Logging {

  var props = new Properties
  if(ENABLE_PROXY_USER.getValue){
    Utils.defaultScheduler.scheduleAtFixedRate(new Runnable {
      override def run(): Unit = {
        info("loading proxy users.")
        val newProps = new Properties
        newProps.load(this.getClass.getResourceAsStream(PROXY_USER_CONFIG.getValue))
        props = newProps
      }
    }, 0, PROXY_USER_SCAN_INTERVAL.getValue, TimeUnit.MILLISECONDS)
  }

  def getProxyUser(umUser: String): String ={
    val proxyUser = props.getProperty(umUser)
    if(StringUtils.isBlank(proxyUser)){
      throw new GatewayErrorException(10033, "No proxy users available(无可用的代理用户)。");
    }
    proxyUser
  }

}
