/*
 * Copyright (C) 2025 FeatJAR-Development-Team
 *
 * This file is part of FeatJAR-formula.
 *
 * formula is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * formula is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with formula. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-formula> for further information.
 */
package de.featjar.analysis;

import de.featjar.base.computation.AComputation;
import de.featjar.base.computation.Dependency;
import de.featjar.base.computation.IComputation;
import de.featjar.base.computation.Progress;
import de.featjar.base.data.Result;
import de.featjar.formula.assignment.BooleanAssignment;
import de.featjar.formula.assignment.BooleanAssignmentList;
import java.util.HashMap;
import java.util.List;

public class ComputeDistributionFeatureSelections extends AComputation<HashMap<String, Integer>> {

    protected static final Dependency<BooleanAssignmentList> BOOLEAN_ASSIGMENT_LIST =
            Dependency.newDependency(BooleanAssignmentList.class);

    public ComputeDistributionFeatureSelections(IComputation<BooleanAssignmentList> booleanAssigmentList) {
        super(booleanAssigmentList);
    }

    @Override
    public Result<HashMap<String, Integer>> compute(List<Object> dependencyList, Progress progress) {
        BooleanAssignmentList booleanAssigmenAssignmentList = BOOLEAN_ASSIGMENT_LIST.get(dependencyList);
        HashMap<String, Integer> selectionDistribution = new HashMap<String, Integer>();
        selectionDistribution.put("selected", 0);
        selectionDistribution.put("deselected", 0);
        selectionDistribution.put("undefined", 0);

        for (BooleanAssignment assignment : booleanAssigmenAssignmentList.getAll()) {
            selectionDistribution.replace(
                    "deselected", assignment.countNegatives() + selectionDistribution.get("deselected"));
            selectionDistribution.replace(
                    "selected", assignment.countPositives() + selectionDistribution.get("selected"));
            selectionDistribution.replace(
                    "undefined",
                    -assignment.countNonZero()
                            + booleanAssigmenAssignmentList
                                    .getVariableMap()
                                    .getVariableNames()
                                    .size()
                            + selectionDistribution.get("undefined"));
        }
        return Result.of(selectionDistribution);
    }
}
