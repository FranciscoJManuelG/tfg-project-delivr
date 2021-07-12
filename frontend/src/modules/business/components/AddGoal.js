import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import GoalTypeSelector from './GoalTypeSelector';
import Sidebar from '../../common/components/BusinessSidebar'
import * as businessSelectors from '../selectors';

const AddGoal = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();
    const [discountType, setdiscountType] = useState('');
    const [discountCash, setDiscountCash] = useState(0);
    const [discountPercentage, setDiscountPercentage] = useState(0);
    const [goalQuantity, setGoalQuantity] = useState(1);
    const [goalTypeId, setGoalTypeId]  = useState(0);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions.addGoal(company.id,
                discountType.trim(), discountCash,
                discountPercentage, toNumber(goalTypeId),
                goalQuantity, 
                () => history.push('business/find-goals'),
                errors => setBackendErrors(errors)));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }
    }

    const toNumber = value => value.length > 0 ? Number(value) : null;

    return (
        <div className="container">
            <Sidebar/>
            <div className="row justify-content-center" >
                <aside className="col-sm-6">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div className="card">
                        <article className="card-body">
                            <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <div className="form-group">
                                    <label htmlFor="discountCash">Descuento en efectivo: </label>
                                    <input type="number" id="discountCash" min="0.01" step ="0.01" className="form-control" 
                                        value={discountCash}
                                        onChange={e => setDiscountCash(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="discountPercentage">Porcentaje de descuento: </label>
                                    <input type="number" id="discountPercentage" min="1" step ="1" className="form-control" 
                                        value={discountPercentage}
                                        onChange={e => setDiscountPercentage(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div className="form-group">
                                    <label htmlFor="goalQuantity">Objetivo: </label>
                                    <input type="number" id="goalQuantity" min="1" step ="1" className="form-control" 
                                        value={goalQuantity}
                                        onChange={e => setGoalQuantity(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div className="form-group">
                                    <label htmlFor="goalTypeId">Tipo de objetivo: </label>
                                    <GoalTypeSelector id="goalTypeId" className="custom-select my-1 mr-sm-2"
                                    value={goalTypeId} 
                                    onChange={e => setGoalTypeId(e.target.value)}/>
                                </div>
                                <div>
                                    <label htmlFor="homeSale">Tipo de descuento: </label>
                                    <div className="form-check-inline">
                                        <input type="radio" id="discountType" name="discountType" className="form-check-input" 
                                            value={"CASH"}
                                            onChange={e => setdiscountType(e.target.value)}
                                            autoFocus
                                            checked
                                            required/>
                                        <label className="form-check-label" htmlFor="discountType">Dinero efectivo</label>
                                    </div>
                                    <div className="form-check-inline">
                                        <input type="radio" id="discountType2" name="discountType" className="form-check-input" 
                                            value={"PERCENTAGE"}
                                            onChange={e => setdiscountType(e.target.value)}
                                            autoFocus
                                            required/>
                                        <label className="form-check-label" htmlFor="discountType2">Porcentaje</label>
                                    </div>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <br/>
                                <div className="row">
                                    <div className="col">
                                        <div className="form-group">
                                            <button type="submit" className="btn btn-primary btn-block">Guardar</button>
                                        </div> 
                                    </div>
                                </div>                                                              
                            </form>
                        </article>
                    </div>
	            </aside> 
            </div>
        </div> 
    );

}

export default AddGoal;