import React from 'react';
import {useSelector} from 'react-redux';

import * as selectors from '../selectors';
import OrderLink from './OrderLink';

const PurchaseCompleted = () => {

    const orderId = useSelector(selectors.getLastOrderId);

    if (!orderId) {
        return null;
    }
    
    return (
        <div className="alert alert-success" role="alert">
            Compra realizada con éxito, ¡Muchas gracias por su confianza!
            &nbsp;
            <OrderLink id={orderId}/>
        </div>
    );

}

export default PurchaseCompleted;
