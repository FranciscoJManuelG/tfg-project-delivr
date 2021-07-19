import React from 'react';
import PropTypes from 'prop-types';
import {FormattedDate, FormattedTime} from 'react-intl';

const DiscountTicket = ({discountTicket}) => {

    return (
        <div className="card col-lg-5 mt-3 mr-2 ml-2" style={{display:'inline-block'}}>
            <div className="card-body">
                <h4 className="card-title">{discountTicket.code}</h4>
                <h6 className="card-text">Disponible hasta: <FormattedDate value={new Date(discountTicket.expirationDate)}/> - <FormattedTime value={new Date(discountTicket.expirationDate)}/></h6>
                {discountTicket.discountPercentage === 0 ?
                    <h6 className="card-text">Dispones de un descuento de: {discountTicket.discountCash}€</h6>
                :
                    <h6 className="card-text">Dispones de un descuento de: {discountTicket.discountPercentage}%</h6>
                }
                <p className="card-subtitle mb-2 text-muted">
                    Lo puedes usar en un pedido para: {discountTicket.companyName}
                </p>
            </div>
        </div>
    );

}

DiscountTicket.propTypes = {
    discountTicket: PropTypes.object.isRequired,
}

export default DiscountTicket;
