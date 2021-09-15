INSERT INTO public.type_of_equipment (id, name) VALUES
    (nextval('type_of_equipment_sequence'), 'тонировка всех стёкол'),
    (nextval('type_of_equipment_sequence'), 'антикоррозийная обработка'),
    (nextval('type_of_equipment_sequence'), 'коврики в салон'),
    (nextval('type_of_equipment_sequence'), 'сигнализация')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO public.vehicle_model (id, name) VALUES
    (nextval('vehicle_model_sequence'), 'Basic'),
    (nextval('vehicle_model_sequence'), 'Standard'),
    (nextval('vehicle_model_sequence'), 'Premium'),
    (nextval('vehicle_model_sequence'), 'Luxury')
    ON CONFLICT (name) DO NOTHING;