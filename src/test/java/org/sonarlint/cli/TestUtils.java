/*
 * SonarLint CLI
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
package org.sonarlint.cli;

import java.nio.file.Paths;
import org.sonarlint.cli.report.IssueWithId;
import org.sonarsource.sonarlint.core.client.api.common.analysis.ClientInputFile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {
  public static IssueWithId createTestIssue(String filePath, String ruleKey, String severity, int line) {
    ClientInputFile inputFile = mock(ClientInputFile.class);
    when(inputFile.getPath()).thenReturn(Paths.get(filePath));

    IssueWithId issue = mock(IssueWithId.class);
    when(issue.getStartLine()).thenReturn(line);
    when(issue.getInputFile()).thenReturn(inputFile);
    when(issue.getRuleKey()).thenReturn(ruleKey);
    when(issue.getSeverity()).thenReturn(severity);
    return issue;
  }
}
