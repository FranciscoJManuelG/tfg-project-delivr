import React from 'react';
import PropTypes from 'prop-types';

const Goals = ({goals, goalTypes, company, onChangeStateItem, onBackendErrors}) => {

    const [backendErrors, setBackendErrors] = useState(null);

    return(

        <div>

            <Errors errors={backendErrors}
                onClose={() => setBackendErrors(null)}/>

            {goals.map(goal => 
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
    goals: PropTypes.array.isRequired,
    goalTypes: PropTypes.array.isRequired
};

export default Goals;
