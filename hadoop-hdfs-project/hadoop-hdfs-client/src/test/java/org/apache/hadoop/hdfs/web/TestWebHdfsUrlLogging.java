/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfs.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class TestWebHdfsUrlLogging {

  @Test
  public void testMaskedUrlForLog() throws IOException {
    assertEquals("http://test/Abc",
        WebHdfsFileSystem.getMaskedUrlForLog(new URL("http://test/Abc")));

    assertEquals("http://test/Abc?op=OPEN&length=99",
        WebHdfsFileSystem.getMaskedUrlForLog(
            new URL("http://test/Abc?op=OPEN&length=99")));

    assertEquals("http://test/Abc?delegation=XXXXX&op=OPEN",
        WebHdfsFileSystem.getMaskedUrlForLog(
            new URL("http://test/Abc?delegation=secret&op=OPEN")));

    assertEquals("http://test/Abc?op=OPEN&Token=XXXXX&length=99",
        WebHdfsFileSystem.getMaskedUrlForLog(
            new URL("http://test/Abc?op=OPEN&Token=secret&length=99")));

    assertEquals("http://test/Abc?token=XXXXX&op=OPEN&token=XXXXX",
        WebHdfsFileSystem.getMaskedUrlForLog(
            new URL("http://test/Abc?token=first&op=OPEN&token=second")));

    assertEquals("http://test/Abc?token=&delegation=&op=OPEN",
        WebHdfsFileSystem.getMaskedUrlForLog(
            new URL("http://test/Abc?token=&delegation=&op=OPEN")));

    final String similarNames =
        "http://test/Abc?mytoken=secret&delegationx=secret&op=OPEN";
    assertEquals(similarNames,
        WebHdfsFileSystem.getMaskedUrlForLog(new URL(similarNames)));

    final String masked = WebHdfsFileSystem.getMaskedUrlForLog(new URL(
        "http://test/Abc?op=OPEN&delegation=secret&token=another"));
    assertFalse(masked.contains("secret"));
    assertFalse(masked.contains("another"));
  }
}
