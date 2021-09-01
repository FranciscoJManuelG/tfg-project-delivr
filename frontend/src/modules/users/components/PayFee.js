import React, {useState} from 'react';

import {Errors} from '../../common';
import PayPalFee from '../../common/components/PayPalFee';

const PayFee = () => {

    const [backendErrors, setBackendErrors] = useState(null);
    
   return (
        <div>
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
            <div className="container-fluid">
                <div className="card col-lg-5 mt-3 mr-2 ml-2">
                    <div className="card-body">
                        <h4 className="card-title">Pago de la cuota trimestral</h4>                        
                        <h6 class="mt-4">120€</h6>
                    </div>
                </div>
            </div>
            <div className="form-group row">
                <div className="offset-md-3 col-md-1">
                    <PayPalFee/>
                </div>
            </div>
        </div>
    );

}

export default PayFee;
