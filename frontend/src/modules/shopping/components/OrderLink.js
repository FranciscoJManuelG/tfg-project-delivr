import React from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';

const OrderLink = ({id}) => {

    return (
        <Link to={`/shopping/order-details/${id}`}>
            Recibo del pedido
        </Link>
    );

}

OrderLink.propTypes = {
    id: PropTypes.number.isRequired
};

export default OrderLink;
