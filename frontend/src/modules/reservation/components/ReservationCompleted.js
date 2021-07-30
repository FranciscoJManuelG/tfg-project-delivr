import React from 'react';
import {useSelector} from 'react-redux';

import * as selectors from '../selectors';
import ReserveLink from './ReserveLink';

const PurchaseCompleted = () => {

    const reserveId = useSelector(selectors.getLastReserveId);

    if (!reserveId) {
        return null;
    }
    
    return (
        <div className="alert alert-success" role="alert">
            Reserva realizada con éxito, ¡Muchas gracias por su confianza!
            &nbsp;
            <ReserveLink id={reserveId}/>
        </div>
    );

}

export default PurchaseCompleted;
