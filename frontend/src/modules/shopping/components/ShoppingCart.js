import React, {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import ShoppingItemList from './ShoppingItemList';
import * as selectors from '../selectors';
import * as actions from '../actions';
import {Errors} from '../../common';

const ShoppingCart = ({companyId}) => {

    const cart = useSelector(selectors.getShoppingCart);
    const dispatch = useDispatch();
    const [homeSale, setHomeSale] = useState(false);
    const [backendErrors, setBackendErrors] = useState(null);
    const history = useHistory();
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        dispatch(actions.changeShoppingCartHomeSale(
            cart.id, companyId, homeSale,
            () => setBackendErrors(null),
            errors => setBackendErrors(errors)
        ));

    }

    return (

        <div>
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
            
                <div>
                <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary"
                            value={true}
                            onChange={e => setHomeSale(e.target.value)}
                            autoFocus>
                                Domicilio
                            </button>
                        </div> 
                        <div className="form-group">
                            <button type="submit" className="btn btn-success"
                            value={false}
                            onChange={e => setHomeSale(e.target.value)}
                            autoFocus>
                                Recogida
                            </button>
                        </div> 
                        <div className="invalid-feedback">
                            <FormattedMessage id='project.global.validator.required'/>
                        </div>
                        </form>
                </div>
                
           
            <ShoppingItemList list={cart} edit companyId = {companyId}
                onUpdateQuantity={(...args) => dispatch(actions.updateShoppingCartItemQuantity(...args))}
                onRemoveItem={(...args) => dispatch(actions.removeShoppingCartItem(...args))}/>

            {cart.items.length > 0 &&

                <div className="text-center">

                    { cart.homeSale ?
                        <button type="button" className="btn btn-primary"
                            onClick={() => history.push(`/shopping/show-fav-addresses/${companyId}`)}>
                            Entrega a domicilio
                        </button>
                    :
                        <button type="button" className="btn btn-primary"
                            onClick={() => history.push(`/shopping/show-fav-addresses/${companyId}`)}>
                            Recoger
                        </button>
                    }
                    
                </div>
            }

        </div>

    );

}
export default ShoppingCart;
