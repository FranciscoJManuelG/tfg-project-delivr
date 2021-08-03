import React, {useState} from 'react';
import {FormattedMessage} from 'react-intl';
import {useSelector} from 'react-redux';
import PropTypes from 'prop-types';

import Reserve from './Reserve';
import users from '../../users';
import {Errors} from '../../common';

const Reserves = ({reserves, onCancelReservation}) => {

    const role = useSelector(users.selectors.getRole);
    const [backendErrors, setBackendErrors] = useState(null);

    return (
        <div>
            <Errors errors={backendErrors}
                onClose={() => setBackendErrors(null)}/>

            <table className="table table-striped table-hover">
                <thead>
                    <tr>
                        <th scope="col">
                        </th>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.date'/>
                        </th>
                        <th scope="col">
                        </th>
                        <th scope="col">
                            Comensales
                        </th>
                        {role === "CLIENT" && 
                            <th scope="col">
                            </th>
                        }
                    </tr>
                </thead>
                <tbody>
                    {reserves.map(reserve => 
                            <Reserve key={reserve.id}
                            reserve={reserve}
                            role={role}
                            onCancelReservation={onCancelReservation}
                            onBackendErrors={errors => setBackendErrors(errors)}
                            />
                            
                    )}
                </tbody>
            </table>    
            
        </div>
    );
};

Reserves.propTypes = {
    reserves: PropTypes.array.isRequired,
    onCancelReservation: PropTypes.func,
    onBackendErrors: PropTypes.func
};

export default Reserves;
