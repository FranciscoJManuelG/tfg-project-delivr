import React from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';

const ReserveLinkCanceled = ({id}) => {

    return (
        <Link to={`/reservation/refund-reserve-details/${id}`}>
            Realizar reembolso
        </Link>
    );

}

ReserveLinkCanceled.propTypes = {
    id: PropTypes.number.isRequired
};

export default ReserveLinkCanceled;
