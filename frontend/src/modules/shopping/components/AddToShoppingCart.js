import React, {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import {Errors} from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const AddToShoppingCart = ({productId, companyId}) => {

    const shoppingCart = useSelector(selectors.getShoppingCart);
    const dispatch = useDispatch();
    const [quantity, setQuantity] = useState(1);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.addToShoppingCart(shoppingCart.id, 
                productId, companyId, quantity,
                () => setBackendErrors(null),
                errors => setBackendErrors(errors)));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }

    }

    return (
        <div>
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
            <form ref={node => form = node}
                className="needs-validation" noValidate 
                onSubmit={e => handleSubmit(e)}>
                <div className="form-group row">
                    <label htmlFor="quantity" className="col-form-label">
                        Cantidad: 
                    </label>
                    <div className="col-md-4">
                        <input type="number" id="quantity" className="form-control"
                            value={quantity}
                            onChange={e => setQuantity(Number(e.target.value))}
                            autoFocus
                            min="1" />
                        <div className="invalid-feedback">
                            <FormattedMessage id='project.global.validator.incorrectQuantity'/>
                        </div>
                    </div>
                    <div>
                        <button type="submit" className="btn btn-primary float-right">
                            Añadir
                        </button>
                    </div>
                </div>
            </form>
        </div>
    );

}


AddToShoppingCart.propTypes = {
    productId: PropTypes.number.isRequired
};

export default AddToShoppingCart;
