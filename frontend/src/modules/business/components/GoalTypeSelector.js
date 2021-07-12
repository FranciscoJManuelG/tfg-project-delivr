import React from 'react';
import {useSelector} from 'react-redux';
import PropTypes from 'prop-types';

import * as selectors from '../selectors';

const GoalTypeSelector = (selectProps) => {

    const goalTypes = useSelector(selectors.getGoalTypes);
    
    return (

        <select {...selectProps}>

            {goalTypes && goalTypes.map(goalType => 
                <option key={goalType.id} value={goalType.id}>{goalType.goalName}</option>
            )}

        </select>

    );

}

GoalTypeSelector.propTypes = {
    selectProps: PropTypes.object
};

export default GoalTypeSelector;
