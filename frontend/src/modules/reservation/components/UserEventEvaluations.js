import React from 'react';
import {FormattedMessage, FormattedDate} from 'react-intl';
import {Link} from 'react-router-dom';

import PropTypes from 'prop-types';

const UserEventEvaluations = ({userEventEvaluations}) => (

    <table className="table table-striped table-hover">

        <thead>
            <tr>
                <th scope="col">
                </th>
                <th scope="col">
                    Restaurante
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.date'/>
                </th>
                <th scope="col">
                </th>
            </tr>
        </thead>

        <tbody>
            {userEventEvaluations.map(userEventEvaluation => 
                <tr key={userEventEvaluation.id}>
                    <td>
                        <span>
                            <Link to={`/reservation/add-event-evaluation/${userEventEvaluation.id}`}>
                                <div className="form-group">
                                    <button type="submit" className="btn btn-primary btn-sm">
                                        Añadir valoración
                                    </button>
                                </div> 
                            </Link>
                        </span>
                    </td>
                    <td>
                        {userEventEvaluation.companyName}
                    </td>
                    <td>
                        <FormattedDate value={new Date(userEventEvaluation.reservationDate)}/>
                    </td>
                    <td>
                        {userEventEvaluation.periodType === 'LUNCH' ? "Comida" : "Cena"}
                    </td>
                </tr>
            )}
        </tbody>

    </table>

);

UserEventEvaluations.propTypes = {
    userEventEvaluations: PropTypes.array.isRequired,
};

export default UserEventEvaluations;
