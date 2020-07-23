import React from 'react';
import PropTypes from 'prop-types';

import * as selectors from '../selectors';

const Products = ({products, productCategories}) => (

    <div className="container-fluid">
        {products.map(product => 
                <div key={product.id} className="card col-lg-5 mt-3 mr-2 ml-2" style={{display:'inline-block'}}>
                    <div className="card-body">
                        <h4 className="card-title">{product.name}</h4>
                        <h6 className="card-subtitle mb-2 text-muted">{selectors.getProductCategoryName(productCategories, product.productCategoryId)}</h6>
                        <p className="card-text">
                            {product.description}
                        </p>
                        <div className="justify-content-between align-items-center">
                            <div>
                                <p>{product.price}</p>
                            </div>
                        </div>
                    </div>
                </div>
        )}
    </div>

);

Products.propTypes = {
    products: PropTypes.array.isRequired,
    productCategories: PropTypes.array.isRequired
};

export default Products;
