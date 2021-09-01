import React from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import ReserveCanceled from './ReserveCanceled';

const ReservesCanceled = ({reserves}) => {

    return (
        <div>
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
                    </tr>
                </thead>
                <tbody>
                    {reserves.map(reserve => 
                            <ReserveCanceled key={reserve.id}
                            reserve={reserve}
                            />   
                    )}
                </tbody>
            </table>    
            
        </div>
    );
};

ReservesCanceled.propTypes = {
    reserves: PropTypes.array.isRequired,
};

export default ReservesCanceled;
