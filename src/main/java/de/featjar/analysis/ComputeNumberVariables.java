package de.featjar.analysis;

import java.util.List;

import de.featjar.base.computation.AComputation;
import de.featjar.base.computation.Dependency;
import de.featjar.base.computation.IComputation;
import de.featjar.base.computation.Progress;
import de.featjar.base.data.Result;
import de.featjar.formula.assignment.BooleanAssignmentList;

public class ComputeNumberVariables extends AComputation<Integer>{

	protected static final Dependency<BooleanAssignmentList> BOOLEAN_ASSIGMENT_LIST = Dependency.newDependency(BooleanAssignmentList.class);

    public ComputeNumberVariables(IComputation<BooleanAssignmentList> booleanAssigmentList) {
        super(booleanAssigmentList);
    }
	
	@Override
	public Result<Integer> compute(List<Object> dependencyList, Progress progress) {
		BooleanAssignmentList booleanAssigmenAssignmentList = BOOLEAN_ASSIGMENT_LIST.get(dependencyList);
		return Result.of(booleanAssigmenAssignmentList.getVariableMap().getVariableNames().size());
	}


}
