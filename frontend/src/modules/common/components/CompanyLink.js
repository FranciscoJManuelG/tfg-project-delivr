import React from 'react';
import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';

const CompanyLink = ({id, name}) => {

    return (
        <Link to={`/shopping/find-shopping-cart-products/${id}`}>
            {name}
        </Link>
    );

}

CompanyLink.propTypes = {
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
};

export default CompanyLink; 
