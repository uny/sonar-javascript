/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011 SonarSource and Eriks Nukis
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.javascript.unittest.surefireparser;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UnitTestClassReportTest {
  @Test
  public void shouldExportToXml() {
    UnitTestClassReport report = new UnitTestClassReport();
    report.add(new UnitTestResult().setStatus(UnitTestResult.STATUS_ERROR).setDurationMilliseconds(500L));
    report.add(new UnitTestResult().setStatus(UnitTestResult.STATUS_OK).setDurationMilliseconds(200L));

    String xml = report.toXml();

    assertThat(xml, is("<tests-details><testcase status=\"error\" time=\"500\" name=\"null\"><error message=\"null\"><![CDATA[null]]></error></testcase><testcase status=\"ok\" time=\"200\" name=\"null\"/></tests-details>"));
  }

  @Test
  public void shouldIncrementCounters() {
    UnitTestClassReport report = new UnitTestClassReport();
    report.add(new UnitTestResult().setStatus(UnitTestResult.STATUS_ERROR).setDurationMilliseconds(500L));
    report.add(new UnitTestResult().setStatus(UnitTestResult.STATUS_OK).setDurationMilliseconds(200L));
    report.add(new UnitTestResult().setStatus(UnitTestResult.STATUS_SKIPPED));

    assertThat(report.getResults().size(), is(3));
    assertThat(report.getSkipped(), is(1L));
    assertThat(report.getTests(), is(3L));
    assertThat(report.getDurationMilliseconds(), is(500L + 200L));
    assertThat(report.getErrors(), is(1L));
    assertThat(report.getFailures(), is(0L));
  }

  @Test
  public void shouldHaveEmptyReport() {
    UnitTestClassReport report = new UnitTestClassReport();
    assertThat(report.getResults().size(), is(0));
    assertThat(report.getSkipped(), is(0L));
    assertThat(report.getTests(), is(0L));
    assertThat(report.getDurationMilliseconds(), is(0L));
    assertThat(report.getErrors(), is(0L));
    assertThat(report.getFailures(), is(0L));
  }

}
