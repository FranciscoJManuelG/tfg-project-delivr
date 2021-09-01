import React from 'react';
import {FormattedDate} from 'react-intl';
import PropTypes from 'prop-types';

import ReserveLinkCanceled from './ReserveLinkCanceled';

const ReserveCanceled = ({reserve}) => {

    return (
        <tr>                   
            <td>
                <ReserveLinkCanceled id={reserve.id}/>
            </td>
            <td>
                <FormattedDate value={new Date(reserve.date)}/> 
            </td>
            <td>
                {reserve.periodType === 'LUNCH' ?
                    'Comida' : 'Cena'}
            </td>
        </tr>
    );

}

ReserveCanceled.propTypes = {
    reserve: PropTypes.object.isRequired,
}

export default ReserveCanceled;
