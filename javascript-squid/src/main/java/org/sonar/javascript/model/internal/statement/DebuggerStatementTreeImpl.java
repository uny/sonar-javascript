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
package org.sonar.javascript.model.internal.statement;

import com.google.common.collect.Iterators;
import org.sonar.javascript.model.internal.JavaScriptTree;
import org.sonar.javascript.model.internal.lexical.InternalSyntaxToken;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.javascript.api.tree.statement.DebuggerStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.EndOfStatementTree;
import org.sonar.plugins.javascript.api.visitors.TreeVisitor;

import java.util.Iterator;

public class DebuggerStatementTreeImpl extends JavaScriptTree implements DebuggerStatementTree {

  private final SyntaxToken debuggerKeyword;
  private final EndOfStatementTree eos;

  public DebuggerStatementTreeImpl(InternalSyntaxToken debuggerKeyword, EndOfStatementTreeImpl eos) {
    super(Kind.DEBUGGER_STATEMENT);
    this.debuggerKeyword = debuggerKeyword;
    this.eos = eos;

    addChild(debuggerKeyword);
    addChild(eos);
  }

  @Override
  public Kind getKind() {
    return Kind.DEBUGGER_STATEMENT;
  }

  @Override
  public SyntaxToken debuggerKeyword() {
    return debuggerKeyword;
  }

  @Override
  public EndOfStatementTree endOfStatement() {
    return eos;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(debuggerKeyword, eos);
  }

  @Override
  public void accept(TreeVisitor visitor) {
    visitor.visitDebugger(this);
  }
}
