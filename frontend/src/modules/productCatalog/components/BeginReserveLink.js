import React from 'react';
import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';

const BeginReserveLink = ({id}) => {

    return (
        <Link to={`/reservation/set-date-and-diners/${id}`}>
            Realizar una reserva
        </Link>
    );

}

BeginReserveLink.propTypes = {
    id: PropTypes.number.isRequired,
};

export default BeginReserveLink; 
