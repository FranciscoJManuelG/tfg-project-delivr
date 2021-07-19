import React from 'react';
import {useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import * as selectors from '../selectors';

const GoalTypeSelector = (selectProps) => {

    const goalTypes = useSelector(selectors.getGoalTypes);
    
    return (

        <select {...selectProps}>

            <FormattedMessage id='project.business.GoalTypeSelector.selectOption'>
                {message => (<option value="">{message}</option>)}
            </FormattedMessage>


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
