import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import * as businessSelectors from '../selectors';
import GoalTypeSelector from './GoalTypeSelector';
import Sidebar from '../../common/components/BusinessSidebar'
import {Errors} from '../../common';
import * as actions from '../actions';

const EditGoal = () => {

    const goal1 = useSelector(businessSelectors.getGoal);
    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();
    
    const [discountType, setdiscountType] = useState('CASH');
    const [discountCash, setDiscountCash] = useState(goal1.discountCash);
    const [discountPercentage, setDiscountPercentage] = useState(goal1.discountPercentage);
    const [goalQuantity, setGoalQuantity] = useState(goal1.goalQuantity);
    const [goalTypeId, setGoalTypeId]  = useState(goal1.goalTypeId);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions,actions.editGoal(
                {
                    id: goal1.id,
                    discountType: discountType.trim(),
                    discountCash: discountCash,
                    discountPercentage: discountPercentage,
                    goalTypeId: toNumber(goalTypeId),
                    goalQuantity:goalQuantity
                },
                company.id,
                () => history.push('/business/find-goals'),
                errors => setBackendErrors(errors)
            ));
            
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
                                    <label htmlFor="discountType">Tipo de descuento: </label>
                                    <div className="form-check-inline">
                                        <input type="radio" id="discountType" name="discountType" className="form-check-input" 
                                            value={'CASH'}
                                            onChange={e => setdiscountType(e.target.value)}
                                            autoFocus
                                            checked/>
                                        <label className="form-check-label" htmlFor="discountType">Dinero efectivo</label>
                                    </div>
                                    <div className="form-check-inline">
                                        <input type="radio" id="discountType" name="discountType2" className="form-check-input" 
                                            value={'PERCENTAGE'}
                                            onChange={e => setdiscountType(e.target.value)}
                                            autoFocus/>
                                        <label className="form-check-label" htmlFor="discountType2">Porcentaje</label>
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

export default EditGoal;