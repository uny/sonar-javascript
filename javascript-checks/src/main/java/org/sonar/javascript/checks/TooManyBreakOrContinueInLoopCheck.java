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
package org.sonar.javascript.checks;

import com.google.common.base.Objects;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.javascript.api.AstTreeVisitorContext;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.declaration.FunctionDeclarationTree;
import org.sonar.plugins.javascript.api.tree.expression.FunctionExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.IdentifierTree;
import org.sonar.plugins.javascript.api.tree.statement.BreakStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.ContinueStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.DoWhileStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.ForInStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.ForOfStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.ForStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.LabelledStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.SwitchStatementTree;
import org.sonar.plugins.javascript.api.tree.statement.WhileStatementTree;
import org.sonar.plugins.javascript.api.visitors.BaseTreeVisitor;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

import java.util.Stack;

@Rule(
  key = "TooManyBreakOrContinueInLoop",
  name = "Loops should not contain more than a single \"break\" or \"continue\" statement",
  priority = Priority.MAJOR,
  tags = {Tags.BRAIN_OVERLOAD})
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("20min")
public class TooManyBreakOrContinueInLoopCheck extends BaseTreeVisitor {

  private static class JumpTarget {
    private final String label;
    private int jumps;

    /**
     * Creates unlabeled target.
     */
    public JumpTarget() {
      this.label = null;
    }

    /**
     * Creates labeled target.
     */
    public JumpTarget(String label) {
      this.label = label;
    }
  }

  private Stack<JumpTarget> jumpTargets = new Stack<>();

  @Override
  public void scanFile(AstTreeVisitorContext context) {
    jumpTargets.clear();
    super.scanFile(context);
  }

  @Override
  public void visitBreakStatement(BreakStatementTree tree) {
    increaseNumberOfJumpInScopes(tree.label());
    super.visitBreakStatement(tree);
  }

  @Override
  public void visitContinueStatement(ContinueStatementTree tree) {
    increaseNumberOfJumpInScopes(tree.label());
    super.visitContinueStatement(tree);
  }

  @Override
  public void visitFunctionExpression(FunctionExpressionTree tree) {
    enterScope();
    super.visitFunctionExpression(tree);
    leaveScope();
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclarationTree tree) {
    enterScope();
    super.visitFunctionDeclaration(tree);
    leaveScope();
  }

  @Override
  public void visitSwitchStatement(SwitchStatementTree tree) {
    enterScope();
    super.visitSwitchStatement(tree);
    leaveScope();
  }

  @Override
  public void visitForStatement(ForStatementTree tree) {
    enterScope();
    super.visitForStatement(tree);
    leaveScopeAndCheckNumberOfJump(tree);
  }

  @Override
  public void visitForInStatement(ForInStatementTree tree) {
    enterScope();
    super.visitForInStatement(tree);
    leaveScopeAndCheckNumberOfJump(tree);
  }

  @Override
  public void visitForOfStatement(ForOfStatementTree tree) {
    enterScope();
    super.visitForOfStatement(tree);
    leaveScopeAndCheckNumberOfJump(tree);
  }

  @Override
  public void visitWhileStatement(WhileStatementTree tree) {
    enterScope();
    super.visitWhileStatement(tree);
    leaveScopeAndCheckNumberOfJump(tree);
  }

  @Override
  public void visitDoWhileStatement(DoWhileStatementTree tree) {
    enterScope();
    super.visitDoWhileStatement(tree);
    leaveScopeAndCheckNumberOfJump(tree);
  }

  @Override
  public void visitLabelledStatement(LabelledStatementTree tree) {
    jumpTargets.push(new JumpTarget(tree.label().name()));
    super.visitLabelledStatement(tree);
    leaveScope();
  }

  private void enterScope() {
    jumpTargets.push(new JumpTarget());
  }

  private void leaveScope() {
    jumpTargets.pop();
  }

  private void increaseNumberOfJumpInScopes(IdentifierTree label) {
    for (int i = jumpTargets.size() - 1; i >= 0; i--) {
      JumpTarget jumpTarget = jumpTargets.get(i);
      String labelName = label == null ? null : label.name();
      jumpTarget.jumps++;

      if (Objects.equal(labelName, jumpTarget.label)) {
        break;
      }
    }
  }

  private void leaveScopeAndCheckNumberOfJump(Tree tree) {
    if (jumpTargets.pop().jumps > 1) {
      getContext().addIssue(this, tree, "Reduce the total number of \"break\" and \"continue\" statements in this loop to use one at most.");
    }
  }

}
