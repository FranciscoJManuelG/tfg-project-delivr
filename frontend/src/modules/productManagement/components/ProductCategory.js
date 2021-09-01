import React from 'react';
import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';


const ProductCategory = ({productCategory}) => {

    return (
        <tr>                   
            <td>
                {productCategory.name}
            </td>
            <td>
                <Link to={`/business/find-product-category-to-edit/${productCategory.id}`}>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary btn-sm float-center">
                                <span className="fas fa-edit"></span>
                            </button>
                        </div> 
                </Link>
            </td>
        </tr>
    );

}

ProductCategory.propTypes = {
    productCategory: PropTypes.object.isRequired
}

export default ProductCategory;
