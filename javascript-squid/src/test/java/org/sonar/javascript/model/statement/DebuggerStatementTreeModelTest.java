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
package org.sonar.javascript.model.statement;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;
import org.sonar.javascript.api.EcmaScriptKeyword;
import org.sonar.javascript.model.JavaScriptTreeModelTest;
import org.sonar.plugins.javascript.api.tree.Tree.Kind;
import org.sonar.plugins.javascript.api.tree.statement.DebuggerStatementTree;

public class DebuggerStatementTreeModelTest extends JavaScriptTreeModelTest {

  @Test
  public void test() throws Exception {
   DebuggerStatementTree tree = parse("debugger;", Kind.DEBUGGER_STATEMENT);

    assertThat(tree.is(Kind.DEBUGGER_STATEMENT)).isTrue();
    assertThat(tree.debuggerKeyword().text()).isEqualTo(EcmaScriptKeyword.DEBUGGER.getValue());
    assertThat(tree.endOfStatement().hasSemicolon()).isTrue();
  }

}
