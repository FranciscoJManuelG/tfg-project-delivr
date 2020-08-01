import React from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router-dom';

import * as selectors from '../selectors';

const Product = ({product, productCategories, company, onDeleteItem, onBlockItem, onUnlockItem, onBackendErrors}) => {

    const handleDeleteItem = () => {

        onDeleteItem(company.id, product.id,
            () => onBackendErrors(null), 
            backendErrors => onBackendErrors(backendErrors));

    }

    const handleBlockItem = () => {

        onBlockItem(product.id, company.id,
            () => onBackendErrors(null));

    }

    const handleUnlockItem = () => {

        onUnlockItem(product.id, company.id,
            () => onBackendErrors(null));

    }

    return (
        <div className="card col-lg-5 mt-3 mr-2 ml-2" style={{display:'inline-block'}}>
            <div className="card-body">
                { product.path == null ?
                    <img class="card-img" src="/img/default.jpg" alt="default"/>
                    :
                    <img class="card-img" src={product.path} alt="product"/>
                }
                <h4 className="card-title">{product.name}</h4>
                <h6 className="card-text">{selectors.getProductCategoryName(productCategories, product.productCategoryId)}</h6>
                <p className="card-subtitle mb-2 text-muted">
                    {product.description}
                </p>
                <div class="price text-success">
                    <h5 class="mt-4">{product.price}€</h5>
                </div>
                {product.block === false ?
                    <span>
                        <button type="submit" className="btn btn-danger btn-sm"
                            onClick={() => handleBlockItem()}>
                            <span className="fas fa-lock"></span>
                        </button>
                        &nbsp;
                        &nbsp;
                    </span>
                :
                    <span>
                        <button type="submit" className="btn btn-success btn-sm"
                            onClick={() => handleUnlockItem()}>
                            <span className="fas fa-lock-open"></span>
                        </button>
                        &nbsp;
                        &nbsp;
                    </span>
                }
                <span>
                    <button type="submit" className="btn btn-danger btn-sm float-right"
                        onClick={() => handleDeleteItem()}>
                        <span className="fas fa-trash-alt"></span>
                    </button>
                    &nbsp;
                    &nbsp;
                </span>
                <span>
                    <Link to={`/management/find-product-to-edit/${product.id}`}>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary btn-sm float-right">
                                <span className="fas fa-edit"></span>
                            </button>
                        </div> 
                    </Link>
                </span>
            </div>
        </div>
    );

}

Product.propTypes = {
    product: PropTypes.object.isRequired,
    productCategories: PropTypes.array.isRequired,
    onDeleteItem: PropTypes.func,
    onBlockItem: PropTypes.func,
    onUnlockItem: PropTypes.func,
    onBackendErrors: PropTypes.func
}

export default Product;
