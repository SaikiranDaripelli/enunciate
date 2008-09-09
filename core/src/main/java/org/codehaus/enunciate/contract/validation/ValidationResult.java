/*
 * Copyright 2006-2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.enunciate.contract.validation;

import com.sun.mirror.util.SourcePosition;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.MemberDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of validation.
 *
 * @author Ryan Heaton
 */
public class ValidationResult {

  private final List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
  private final List<ValidationMessage> warnings = new ArrayList<ValidationMessage>();

  /**
   * Whether there are any errors in the result.
   *
   * @return Whether there are any errors in the result.
   */
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  /**
   * Add an error message.
   *
   * @param position The source position.
   * @param text     The text of the error message.
   * @deprecated Use {@link #addError(com.sun.mirror.declaration.Declaration, String)}
   */
  public void addError(SourcePosition position, String text) {
    this.errors.add(new ValidationMessage(position, text));
  }

  /**
   * Add an error message.
   *
   * @param declaration The position of the error.
   * @param text     The text of the error message.
   */
  public void addError(Declaration declaration, String text) {
    if (declaration == null) {
      this.errors.add(new ValidationMessage(null, text));
    }
    else if (declaration.getPosition() != null) {
      this.errors.add(new ValidationMessage(declaration.getPosition(), text));
    }
    else {
      this.errors.add(new ValidationMessage(null, toString(declaration) + ": " + text));
    }
  }

  /**
   * The errors.
   *
   * @return The errors.
   */
  public List<ValidationMessage> getErrors() {
    return errors;
  }

  /**
   * Whether there are any warnings in the result.
   *
   * @return Whether there are any warnings in the result.
   */
  public boolean hasWarnings() {
    return !warnings.isEmpty();
  }

  /**
   * Add an warning message.
   *
   * @param position The source position.
   * @param text     The text of the warning message.
   * @deprecated Use {@link #addWarning(com.sun.mirror.declaration.Declaration, String)}
   */
  public void addWarning(SourcePosition position, String text) {
    this.warnings.add(new ValidationMessage(position, text));
  }

  /**
   * Add n warning message.
   *
   * @param declaration The position of the warning.
   * @param text     The text of the warning message.
   */
  public void addWarning(Declaration declaration, String text) {
    if (declaration == null) {
      this.errors.add(new ValidationMessage(null, text));
    }
    else if (declaration.getPosition() != null) {
      this.warnings.add(new ValidationMessage(declaration.getPosition(), text));
    }
    else {
      this.warnings.add(new ValidationMessage(null, toString(declaration) + ": " + text));
    }
  }

  /**
   * Whether there are any warnings in the result.
   *
   * @return Whether there are any warnings in the result.
   */
  public List<ValidationMessage> getWarnings() {
    return warnings;
  }

  /**
   * Aggregate the specified result to these results.
   *
   * @param result The result to aggregate.
   */
  public void aggregate(ValidationResult result) {
    this.errors.addAll(result.errors);
    this.warnings.addAll(result.warnings);
  }

  private String toString(Declaration declaration) {
    StringBuilder builder = new StringBuilder();
    if (declaration instanceof TypeDeclaration) {
      builder.append(((TypeDeclaration) declaration).getQualifiedName());
    }
    else if (declaration instanceof MemberDeclaration) {
      if (((MemberDeclaration) declaration).getDeclaringType() != null) {
        builder.append(((MemberDeclaration) declaration).getDeclaringType().getQualifiedName());
        builder.append('.');
      }

      builder.append(declaration.getSimpleName());
    }
    else if (declaration instanceof ParameterDeclaration) {
      builder.append("Parameter ").append(declaration.getSimpleName());
    }
    else {
      builder.append(declaration.getSimpleName());
    }
    return builder.toString();
  }

}