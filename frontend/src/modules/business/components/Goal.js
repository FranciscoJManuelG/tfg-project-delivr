import React from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router-dom';

import * as selectors from '../selectors';

const Goal = ({goal, goalTypes, company, onChangeStateItem, onBackendErrors}) => {


    const handleChangeStateItem = (option) => {

        onChangeStateItem(goal.id, company.id, option,
            () => onBackendErrors(null));

    }

    return (
        <div className="card col-lg-5 mt-3 mr-2 ml-2" style={{display:'inline-block'}}>
            <div className="card-body">
                <h4 className="card-title">{selectors.getGoalTypeGoalName(goalTypes, goal.goalTypeId)}</h4>
                {goal.discountPercentage === 0 ? 
                    <h6 className="card-text">{goal.discountCash}€</h6>
                :
                    <h6 className="card-text">{goal.discountPercentage}%</h6>
                }
                <p className="card-subtitle mb-2 text-muted">
                    Objetivo: {goal.goalQuantity}
                </p>
                {goal.active === true ?
                    <span>
                        <button type="submit" className="btn btn-danger btn-sm"
                            onClick={() => handleChangeStateItem("desactivar")}>
                            <span className="fas fa-lock"></span>
                        </button>
                        &nbsp;
                        &nbsp;
                    </span>
                :
                    <span>
                        <button type="submit" className="btn btn-success btn-sm"
                            onClick={() => handleChangeStateItem("activar")}>
                            <span className="fas fa-lock-open"></span>
                        </button>
                        &nbsp;
                        &nbsp;
                    </span>
                }
                <span>
                    <Link to={`/business/find-goal-to-edit/${goal.id}`}>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary btn-sm float-right">
                                <span className="fas fa-edit"></span>
                            </button>
                        </div> 
                    </Link>
                </span>
            </div>
        </div>
    );

}

Goal.propTypes = {
    goal: PropTypes.object.isRequired,
    goalTypes: PropTypes.array.isRequired,
    company: PropTypes.array.isRequired,
    onChangeStateItem: PropTypes.func
}

export default Goal;
