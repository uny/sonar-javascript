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
import com.sonar.sslr.api.AstNode;
import org.sonar.plugins.javascript.api.visitors.TreeVisitor;
import org.sonar.javascript.model.internal.JavaScriptTree;
import org.sonar.javascript.model.internal.lexical.InternalSyntaxToken;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.declaration.DeclarationTree;
import org.sonar.plugins.javascript.api.tree.declaration.ImportClauseTree;
import org.sonar.plugins.javascript.api.tree.expression.IdentifierTree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxToken;

import javax.annotation.Nullable;
import java.util.Iterator;

public class ImportClauseTreeImpl extends JavaScriptTree implements ImportClauseTree {

  private IdentifierTree defaultImport;
  private SyntaxToken commaToken;
  private DeclarationTree namedImport;

  public ImportClauseTreeImpl(IdentifierTree defaultImport) {
    super(Kind.IMPORT_CLAUSE);
    this.defaultImport = defaultImport;

    addChildren((AstNode) defaultImport);
  }

  public ImportClauseTreeImpl(DeclarationTree namedImport) {
    super(Kind.IMPORT_CLAUSE);
    this.namedImport = namedImport;

    addChildren((AstNode) namedImport);
  }

  public ImportClauseTreeImpl(IdentifierTree defaultImport, InternalSyntaxToken commaToken, DeclarationTree namedImport) {
    super(Kind.IMPORT_CLAUSE);
    this.defaultImport = defaultImport;
    this.commaToken = commaToken;
    this.namedImport = namedImport;

    addChildren((AstNode) defaultImport, commaToken, (AstNode) namedImport);
  }

  @Nullable
  @Override
  public IdentifierTree defaultImport() {
    return defaultImport;
  }

  @Nullable
  @Override
  public SyntaxToken commaToken() {
    return commaToken;
  }

  @Nullable
  @Override
  public DeclarationTree namedImport() {
    return namedImport;
  }

  @Override
  public Kind getKind() {
    return Kind.IMPORT_CLAUSE;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(defaultImport, commaToken, namedImport);
  }

  @Override
  public void accept(TreeVisitor visitor) {
    visitor.visitImportClause(this);
  }
}
