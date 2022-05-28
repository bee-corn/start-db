/*
 * Copyright 2022 ST-Lab
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */

package org.urbcomp.start.db

import org.junit.Assert.{assertEquals, assertNotNull}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import java.net.URLDecoder
import java.sql.DriverManager
import java.util.Properties

/**
  * Test for Calcite and Geomesa
  *
  * @author zaiyuan
  * @since 0.1.0
  */
class CalciteGeomesaTest extends FunSuite with BeforeAndAfterAll {

  var config: Properties = _

  override protected def beforeAll(): Unit = {
    val url = this.getClass.getResource("/model.json")
    val str = URLDecoder.decode(url.toString, "UTF-8")
    config = new Properties
    config.put("model", str.replace("file:", ""))
    config.put("caseSensitive", "false")
  }

  // TODO create table first
  ignore("calcite geomesa test") {
    val connect = DriverManager.getConnection("jdbc:calcite:", config)
    val statement = connect.createStatement
    val resultSet = statement.executeQuery("select * from citibike_tripdata")
    while (resultSet.next()) {
      assertNotNull(resultSet.getObject(1))
    }
  }

  test("calcite geomesa test with simple function") {
    val connect = DriverManager.getConnection("jdbc:calcite:fun=spatial", config)
    val statement = connect.createStatement
    val resultSet = statement.executeQuery("select concat('1', '2')")
    while (resultSet.next()) {
      assertEquals(resultSet.getObject(1), "12")
    }
  }
}
