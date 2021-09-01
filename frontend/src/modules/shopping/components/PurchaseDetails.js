import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useParams} from 'react-router-dom';

import {Errors, Success} from '../../common';
import ShoppingItemList from './ShoppingItemList';
import * as selectors from '../selectors';
import * as usersSelectors from '../../users/selectors';
import * as actions from '../actions';
import PayPalOrder from '../../common/components/PayPalOrder';

const PurchaseDetails = () => {

    const dispatch = useDispatch();
    const cart = useSelector(selectors.getShoppingCart);
    const lastAddress = useSelector(selectors.getLastAddress);
    const user = useSelector(usersSelectors.getUser);
    const [backendErrors, setBackendErrors] = useState(null);
    const [codeDiscount, setCodeDiscount] = useState('');
    const [successMessage, setSuccessMessage] = useState(null);
    const {id} = useParams();
    let form;
    const companyId = id;
    var street;
    var cp;

    if (lastAddress == null) {
        street = null;
        cp = null;
    } else {
        street = lastAddress.street.trim();
        cp = lastAddress.cp.trim();
    }

    const handleRedeemTicket = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.redeemDiscountTicket(
                companyId,
                cart.id,
                codeDiscount,
                () => setSuccessMessage("¡Ticket descuento correcto!"),
                errors => setBackendErrors(errors)
            ));

        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
        }

    }


    if (cart.items.length === 0) {
        return null;
    }
    
   return (
        <div>
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
            <div className="container-fluid">
                <div className="card col-lg-5 mt-3 mr-2 ml-2">
                    <div className="card-body">
                        <h4 className="card-title">{user.firstName} {user.lastName}</h4>
                        {lastAddress != null && <h6 class="mt-4">{lastAddress.street}, {lastAddress.cp}</h6>}
                    </div>
                </div>
                <form ref={node => form = node}
                className="needs-validation" noValidate 
                onSubmit={(e) => handleRedeemTicket(e)}>
                <div className="card">
                    <Success message={successMessage && setBackendErrors(null)} onClose={() => setSuccessMessage(null)}/>
                    <article className="card-body">
                        <input id="codeDiscount" placeholder="Codigo descuento" type="text" className="form-control mr-sm-2"
                            value={codeDiscount} onChange={e => setCodeDiscount(e.target.value)}/>
                        <button type="submit" className="btn btn-primary my-2 my-sm-0">
                            Comprobar
                        </button>
                    </article>
                </div>
            </form>
            </div>
            <ShoppingItemList list={cart}/>
                <div className="form-group row">
                    <div className="offset-md-3 col-md-1">
                        <PayPalOrder totalPrice={cart.totalPrice} cart={cart} companyId={companyId} street={street} cp={cp} codeDiscount={codeDiscount}/>
                    </div>
                </div>
        </div>
    );

}

export default PurchaseDetails;
