import React, {useState} from 'react';
import PropTypes from 'prop-types';

import Goal from './Goal';
import {Errors} from '../../common';

const Goals = ({goalList, goalTypes, company, onChangeStateItem}) => {

    const [backendErrors, setBackendErrors] = useState(null);

    return(

        <div>

            <Errors errors={backendErrors}
                onClose={() => setBackendErrors(null)}/>

            {goalList.map(goal => 
                <Goal key={goal.id}
                    goal={goal}
                    goalTypes={goalTypes}
                    company={company}
                    onChangeStateItem={onChangeStateItem}
                    onBackendErrors={errors => setBackendErrors(errors)}/>
            )}

        </div>

    );
};

Goals.propTypes = {
    goalList: PropTypes.array.isRequired,
    goalTypes: PropTypes.array.isRequired
};

export default Goals;
