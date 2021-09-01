import React from 'react';
import {FormattedDate} from 'react-intl';
import PropTypes from 'prop-types';

import ReserveLink from './ReserveLink';

const Reserve = ({reserve, role, onCancelReservation, onBackendErrors}) => {

    const handleCancelReservation = () => {

        onCancelReservation(reserve.id, 
            () => onBackendErrors(null), 
            backendErrors => onBackendErrors(backendErrors));
    }

    return (
        <tr>                   
            <td>
                <ReserveLink id={reserve.id}/>
            </td>
            <td>
                <FormattedDate value={new Date(reserve.date)}/> 
            </td>
            <td>
                {reserve.periodType === 'LUNCH' ?
                    'Comida' : 'Cena'}
            </td>
            <td>
                {reserve.diners}
            </td>
            {role === "CLIENT" &&
                <td>
                    <span>
                        <button type="submit" className="btn btn-danger btn-sm"
                            onClick={() => {
                                if(window.confirm('¿Estas seguro que deseas cancelar la reserva?')) handleCancelReservation() 
                            }}>
                            Cancelar Reserva
                        </button>
                        &nbsp;
                        &nbsp;
                    </span>
                </td>
            }
        </tr>
    );

}

Reserve.propTypes = {
    reserve: PropTypes.object.isRequired,
    role: PropTypes.object.isRequired,
    onCancelReservation: PropTypes.func,
    onBackendErrors: PropTypes.func
}

export default Reserve;
