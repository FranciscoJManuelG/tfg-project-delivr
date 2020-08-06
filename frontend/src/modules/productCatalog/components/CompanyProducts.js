import React from 'react';
import PropTypes from 'prop-types';

import * as selectors from '../selectors';
import AddToShoppingCart from '../../shopping/components/AddToShoppingCart';

const CompanyProducts = ({products, productCategories}) => (

    <div className="container-fluid">
        {products.map(product => 
                <div key={product.id} className="card col-lg-5 mt-3 mr-2 ml-2" style={{display:'inline-block'}}>
                    <div className="card-body">
                        <img class="card-img" src={product.path} alt="product"/>
                        <h4 className="card-title">{product.name}</h4>
                        <h6 className="card-text">{selectors.getProductCategoryName(productCategories, product.productCategoryId)}</h6>
                        <p className="card-subtitle mb-2 text-muted">
                            {product.description}
                        </p>
                        <div class="price text-success">
                            <h5 class="mt-4">{product.price}€</h5>
                        </div>
                    </div>
                </div>
        )}
        <AddToShoppingCart/>
    </div>

);

CompanyProducts.propTypes = {
    products: PropTypes.array.isRequired,
    productCategories: PropTypes.array.isRequired
};

export default CompanyProducts;
