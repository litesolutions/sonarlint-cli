/*
 * SonarLint CLI
 * Copyright (C) 2016-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarlint.cli.report;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonarsource.sonarlint.core.client.api.common.RuleDetails;
import org.sonarsource.sonarlint.core.client.api.common.analysis.AnalysisResults;
import org.sonarsource.sonarlint.core.client.api.common.analysis.ClientInputFile;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;
import org.sonarsource.sonarlint.core.tracking.IssueTrackable;
import org.sonarsource.sonarlint.core.tracking.Trackable;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class XmlReportTest {
  private XmlReport xml;
  private AnalysisResults result;

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();
  private Path reportFile;

  @Before
  public void setUp() {
    result = mock(AnalysisResults.class);
    when(result.indexedFileCount()).thenReturn(1);
    reportFile = temp.getRoot().toPath().resolve("report.xml");
    xml = new XmlReport(temp.getRoot().toPath(), reportFile, StandardCharsets.UTF_8);
  }

  @Test
  public void testXml() {
    xml.execute("project", new Date(), new LinkedList<>(), result, k -> null);
  }

  @Test
  public void testCopyRuleDesc() {
    xml.execute("project", new Date(), Arrays.asList(createTestIssue(temp.getRoot().toPath().resolve("test.java").toString(), "squid:1234", "bla", "MAJOR", 1)), result,
            k -> "squid:1234".equals(k) ? mockRuleDetails() : null);

    assertThat(reportFile.getParent().resolve("report.xml").toFile())
            .usingCharset(StandardCharsets.UTF_8)
            .hasContent(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<sonarlintreport>\n" +
            "  <files>\n" +
            "      <file name=\"test.java\">\n" +
            "      <issues total=\"1\">\n" +
            "        <issue severity=\"major\" key=\"squid:1234\" name=\"bla\" line=\"1\" offset=\"0\"/>\n" +
            "      </issues>\n" +
            "    </file>\n" +
            "  </files>\n" +
            "</sonarlintreport>");
  }

  private RuleDetails mockRuleDetails() {
    RuleDetails ruleDetails = mock(RuleDetails.class);
    when(ruleDetails.getName()).thenReturn("Foo");
    when(ruleDetails.getHtmlDescription()).thenReturn("foo bar");
    return ruleDetails;
  }

  private static Trackable createTestIssue(String filePath, String ruleKey, String name, String severity, int line) {
    ClientInputFile inputFile = mock(ClientInputFile.class);
    when(inputFile.getPath()).thenReturn(filePath);

    Issue issue = mock(Issue.class);
    when(issue.getStartLine()).thenReturn(line);
    when(issue.getStartLineOffset()).thenReturn(null);
    when(issue.getEndLine()).thenReturn(line);
    when(issue.getEndLineOffset()).thenReturn(null);
    when(issue.getRuleName()).thenReturn(name);
    when(issue.getInputFile()).thenReturn(inputFile);
    when(issue.getRuleKey()).thenReturn(ruleKey);
    when(issue.getSeverity()).thenReturn(severity);
    return new IssueTrackable(issue);
  }
}
