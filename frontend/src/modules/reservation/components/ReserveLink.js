import React from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';

const ReserveLink = ({id}) => {

    return (
        <Link to={`/reservation/reserve-details/${id}`}>
            Detalle de la reserva
        </Link>
    );

}

ReserveLink.propTypes = {
    id: PropTypes.number.isRequired
};

export default ReserveLink;
