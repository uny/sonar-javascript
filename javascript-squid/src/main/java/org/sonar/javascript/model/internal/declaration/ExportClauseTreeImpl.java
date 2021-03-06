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
package org.sonar.javascript.model.internal.declaration;

import com.google.common.collect.Iterators;
import org.sonar.javascript.model.internal.JavaScriptTree;
import org.sonar.javascript.model.internal.statement.EndOfStatementTreeImpl;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.declaration.ExportClauseTree;
import org.sonar.plugins.javascript.api.tree.declaration.FromClauseTree;
import org.sonar.plugins.javascript.api.tree.declaration.SpecifierListTree;
import org.sonar.plugins.javascript.api.tree.statement.EndOfStatementTree;
import org.sonar.plugins.javascript.api.visitors.TreeVisitor;

import java.util.Iterator;

public class ExportClauseTreeImpl extends JavaScriptTree implements ExportClauseTree {

  private final SpecifierListTree exports;
  private final FromClauseTree fromClause;
  private final EndOfStatementTree eos;

  public ExportClauseTreeImpl(SpecifierListTreeImpl exports, EndOfStatementTreeImpl eos) {
    super(Kind.EXPORT_CLAUSE);

    this.exports = exports;
    this.fromClause = null;
    this.eos = eos;

    addChildren(exports, eos);
  }

  public ExportClauseTreeImpl(SpecifierListTreeImpl exports, FromClauseTreeImpl fromClause, EndOfStatementTreeImpl eos) {
    super(Kind.EXPORT_CLAUSE);

    this.exports = exports;
    this.fromClause = fromClause;
    this.eos = eos;

    addChildren(exports, fromClause, eos);
  }

  @Override
  public SpecifierListTree exports() {
    return exports;
  }

  @Override
  public FromClauseTree fromClause() {
    return fromClause;
  }

  @Override
  public EndOfStatementTree eos() {
    return eos;
  }

  @Override
  public Kind getKind() {
    return Kind.EXPORT_CLAUSE;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(exports, fromClause, eos);
  }

  @Override
  public void accept(TreeVisitor visitor) {
    visitor.visitExportClause(this);
  }
}
