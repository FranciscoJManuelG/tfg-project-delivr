import React from 'react';
import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';

const CompanyLink = ({id, name, doReserve, cityId}) => {

    return (
        <Link to={`/shopping/find-shopping-cart-products/${id}/${doReserve}/${cityId}`}>
            {name}
        </Link>
    );

}

CompanyLink.propTypes = {
    id: PropTypes.number.isRequired,
    homeSale: PropTypes.bool.isRequired,
    name: PropTypes.string.isRequired,
};

export default CompanyLink; 
