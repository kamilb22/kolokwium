package edu.iis.mto.oven;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    @Mock
    Fan fan;
    @Mock
    HeatingModule heatingModule;

    Oven oven;

    @BeforeEach
    void setUp(){
        oven = new Oven(heatingModule,fan);
    }

    @Test
    void ifHeatTypeIsThermoCirculationFanShouldBeTurnedOn() {
        int anyTemp = 20;
        ProgramStage programStage = ProgramStage.builder()
                .withHeat(HeatType.THERMO_CIRCULATION)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        List<ProgramStage> stagesList = List.of(programStage);
        BakingProgram bakingProgram = BakingProgram.builder().withInitialTemp(anyTemp).withStages(stagesList).build();
        oven.start(bakingProgram);
        Mockito.verify(fan).on();
    }

    @Test
    void ifHeatTypeIsNotThermoCirculationFanShouldNotBeTurnedOn() {
        int anyTemp = 20;
        ProgramStage programStage1 = ProgramStage.builder()
                .withHeat(HeatType.HEATER)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        ProgramStage programStage2 = ProgramStage.builder()
                .withHeat(HeatType.GRILL)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        List<ProgramStage> stagesList = List.of(programStage1, programStage2);
        BakingProgram bakingProgram = BakingProgram.builder().withInitialTemp(anyTemp).withStages(stagesList).build();
        oven.start(bakingProgram);
        Mockito.verify(fan, Mockito.never()).on();
    }

    @Test
    void fanShouldBeTurnedOffOnEveryStage() {
        int anyTemp = 20;
        ProgramStage programStage1 = ProgramStage.builder()
                .withHeat(HeatType.HEATER)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        ProgramStage programStage2 = ProgramStage.builder()
                .withHeat(HeatType.GRILL)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        ProgramStage programStage3 = ProgramStage.builder()
                .withHeat(HeatType.THERMO_CIRCULATION)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        List<ProgramStage> stagesList = List.of(programStage1, programStage2, programStage3);
        BakingProgram bakingProgram = BakingProgram.builder().withInitialTemp(anyTemp).withStages(stagesList).build();
        oven.start(bakingProgram);
        Mockito.verify(fan,Mockito.times(3)).off();
    }

}
